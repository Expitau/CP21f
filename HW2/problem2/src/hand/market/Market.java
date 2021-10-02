package hand.market;

import hand.agent.Buyer;
import hand.agent.Seller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Pair<K,V> {
    public K key;
    public V value;
    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

public class Market {
    public ArrayList<Buyer> buyers;
    public ArrayList<Seller> sellers;

    public Market(int nb, ArrayList<Double> fb, int ns, ArrayList<Double> fs) {
        buyers = createBuyers(nb, fb);
        sellers = createSellers(ns, fs);
    }
    
    private ArrayList<Buyer> createBuyers(int n, ArrayList<Double> f) {
        //TODO: Problem 2.3
        ArrayList<Buyer> ret = new ArrayList<Buyer>();
        for(int i=1; i<=n; i++){
            Double limit = 0.0;
            //for(int j= f.size()-1; j>=0; j--){
            //    limit = limit*((double)i/(double)n) + f.get(j);
            //}
            for(int j=0; j<f.size(); j++){
                limit += f.get(j)*Math.pow(((double)i/(double)n), j);
            }
            ret.add(new Buyer(limit));
        }
        return ret;
    }


    private ArrayList<Seller> createSellers(int n, ArrayList<Double> f) {
        //TODO: Problem 2.3
        ArrayList<Seller> ret = new ArrayList<Seller>();
        for(int i=1; i<=n; i++){
            Double limit = 0.0;
            //for(int j= f.size()-1; j>=0; j--){
            //    limit = limit*((double)i/(double)n) + f.get(j);
            //}
            for(int j=0; j<f.size(); j++){
                limit += f.get(j)*Math.pow(((double)i/(double)n), j);
            }
            ret.add(new Seller(limit));
        }
        return ret;
    }

    private ArrayList<Pair<Seller, Buyer>> matchedPairs(int day, int round) {
        ArrayList<Seller> shuffledSellers = new ArrayList<>(sellers);
        ArrayList<Buyer> shuffledBuyers = new ArrayList<>(buyers);
        Collections.shuffle(shuffledSellers, new Random(71 * day + 43 * round + 7));
        Collections.shuffle(shuffledBuyers, new Random(67 * day + 29 * round + 11));
        ArrayList<Pair<Seller, Buyer>> pairs = new ArrayList<>();
        for (int i = 0; i < shuffledBuyers.size(); i++) {
            if (i < shuffledSellers.size()) {
                pairs.add(new Pair<>(shuffledSellers.get(i), shuffledBuyers.get(i)));
            }
        }
        return pairs;
    }

    public double simulate() {
        //TODO: Problem 2.2 and 2.3
        double sum=0;
        int cnt=0;
        for (int day = 1; day <= 3000; day++) { // do not change this line
            for (int round = 1; round <= 5; round++) { // do not change this line
                ArrayList<Pair<Seller, Buyer>> pairs = matchedPairs(day, round); // do not change this line
                for(Pair<Seller, Buyer> pair : pairs){
                    Seller seller = pair.key;
                    Buyer buyer = pair.value;
                    if(buyer.willTransact(seller.getExpectedPrice()) && seller.willTransact(seller.getExpectedPrice())){
                        if(day == 3000){
                            sum += seller.getExpectedPrice();
                            cnt += 1;
                        }
                        buyer.makeTransaction();
                        seller.makeTransaction();
                    }
                }
            }
            for(int i=0; i< buyers.size(); i++) buyers.get(i).reflect();
            for(int i=0; i< sellers.size(); i++) sellers.get(i).reflect();
            //for(Buyer buyer : buyers) buyer.reflect();
            //for(Seller seller : sellers) seller.reflect();
        }
        if(cnt == 0) return 0;
        return sum/cnt;
    }
}

