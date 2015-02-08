package claflin.livechair.com.livechair;
import java.util.Random;
/**
 * Created by sakshyamdahal on 2/8/15.
 */
public class Barber {
    int mProfileId;
    String mName;
    int mRating;
    Random number = new Random();


    public Barber(String name, int profileId){
        mName = name;
        mRating = 1 + number.nextInt(5);
        mProfileId = profileId;
    }
}
