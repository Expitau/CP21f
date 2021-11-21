#include "CSI.h"
#include <sstream>

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
    return os;
}

void read_csi(const char* filename, CSI* csi) {
    // TODO: problem 2.2
}

double** decode_csi(CSI* csi) {
    // TODO: problem 2.3
    return nullptr;
}

double* get_med(double** decoded_csi, int num_packets, int packet_length) {
    // TODO: problem 2.4
    return nullptr;
}

double breathing_interval(double** decoded_csi, int num_packets) {
    // TODO: problem 2.5
    return 0;
}
