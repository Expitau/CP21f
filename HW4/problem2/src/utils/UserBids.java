package utils;
import java.util.*;

public class UserBids {
    public String userId;
    public Integer mileage, totMileage;

    public UserBids(String id, Integer m, Integer tm){
        userId = id;
        mileage = m;
        totMileage = tm;
    }
}
