#include "CSI.h"
#include <sstream>
#include <fstream>
#include <algorithm>
#include <cmath>

Complex::Complex(): real(0), imag(0) {}

CSI::CSI(): data(nullptr), num_packets(0), num_channel(0), num_subcarrier(0) {}

CSI::~CSI() {
    if(data) {
        for(int i = 0 ; i < num_packets; i++) {
            delete[] data[i];
        }
        delete[] data;
    }
}

int CSI::packet_length() const {
    return num_channel * num_subcarrier;
}

void CSI::print(std::ostream& os) const {
    for (int i = 0; i < num_packets; i++) {
        for (int j = 0; j < packet_length(); j++) {
            os << data[i][j] << ' ';
        }
        os << std::endl;
    }
}

std::ostream& operator<<(std::ostream &os, const Complex &c) {
    // TODO: problem 2.1
    os << c.real;
    if(c.imag < 0) os << c.imag << "i";
    else os << "+" << c.imag << "i";
    return os;
}

void read_csi(const char* filename, CSI* csi) {
    // TODO: problem 2.2
    std::ifstream input_stream(filename);
    int np, nc, ns;

    input_stream >> np >> nc >> ns;

    csi->num_packets = np;
    csi->num_channel = nc;
    csi->num_subcarrier = ns;
    csi->data = new Complex*[np];
    for(int i = 0; i < csi->num_packets; i++){
        csi->data[i] = new Complex[nc*ns];
        for(int j = 0; j < ns; j++){
            for(int k=0; k < nc; k++) {
                input_stream >> csi->data[i][k * ns + j].real >> csi->data[i][k * ns + j].imag;
            }
        }
    }

}

double get_amplitude(Complex c){
    return sqrt(c.real*c.real + c.imag*c.imag);
}

double** decode_csi(CSI* csi) {
    // TODO: problem 2.3
    double** amp = new double*[csi->num_packets];
    for(int i=0; i< csi->num_packets; i++) {
        amp[i] = new double[csi->packet_length()];
        for (int j = 0; j < csi->packet_length(); j++) {
            amp[i][j] = get_amplitude(csi->data[i][j]);
        }
    }
    return amp;
}

double* get_med(double** decoded_csi, int num_packets, int packet_length) {
    // TODO: problem 2.4
    double* med = new double[num_packets];
    double* temp = new double[packet_length];


    for(int i=0; i<num_packets; i++){
        for(int j=0; j<packet_length; j++) temp[j] = decoded_csi[i][j];
        std::nth_element(temp,  temp + packet_length/2, temp+packet_length);
        std::nth_element(temp,  temp + (packet_length-1)/2, temp+packet_length);
        med[i] = (temp[(packet_length-1)/2] + temp[packet_length/2])/2.0;
    }

    return med;
}

bool check_peek(double** decoded_csi, int num_packets, int now_idx, int next_idx){
    if(next_idx < 0 || next_idx >= num_packets) return true;
    return decoded_csi[now_idx][0] > decoded_csi[next_idx][0] + (double)1e-10;
}

double breathing_interval(double** decoded_csi, int num_packets) {
    // TODO: problem 2.5

    int last_peek = -1;
    int interval_sum = 0;
    int interval_cnt = 0;

    for(int i=0; i<num_packets; i++){
        bool flag = true;
        for(int j=1; flag && j<=2; j++){
            if(!check_peek(decoded_csi, num_packets, i, i+j)) flag = false;
            if(!check_peek(decoded_csi, num_packets, i, i-j)) flag = false;
        }
        if(flag){
            if(last_peek != -1){
                interval_sum += i - last_peek;
                interval_cnt++;
            }
            last_peek = i;
        }
    }

    if(interval_cnt == 0) return num_packets;
    return (double)interval_sum/interval_cnt;
}
