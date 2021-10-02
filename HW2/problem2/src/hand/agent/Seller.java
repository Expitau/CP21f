package hand.agent;

public class Seller extends Agent {

    public Seller(double minimumPrice) {
        super(minimumPrice);
    }

    @Override
    public boolean willTransact(double price) {
        //TODO: Problem 2.1
        return !hadTransaction && price >= expectedPrice;
    }

    @Override
    public void reflect() {
        //TODO: Problem 2.1
        if(hadTransaction){
            expectedPrice += adjustment;
            adjustment += 5.0;
            if(adjustment >= adjustmentLimit){
                adjustment = adjustmentLimit;
            }
        }else{
            expectedPrice -= adjustment;
            if(expectedPrice < priceLimit) {
                expectedPrice = priceLimit;
            }else{
                adjustment -= 5.0;
                if(adjustment < 0.0) {
                    adjustment = 0.0;
                }
            }
        }
        hadTransaction = false;
    }
}
