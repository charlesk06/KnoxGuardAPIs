import com.samsung.knoxwsm.util.KnoxTokenUtility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AccessToken {

    public static void main(String[] args) throws FileNotFoundException {
        // TODO Auto-generated method stub
        String cert = "src/test/resources/keys.json";
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJjb250ZXh0IjoiMjRiZDJlZTItYjFmNC00NDI0LThmYjktNWNkZGJmODM1OWZlN2UzZDVjMzUtMzk4ZS00NjkzLThhNTMtNmM0OWU2ODNiYzkxIiwiYWNjZXNzVG9rZW4iOiJkY2M1NDYwNS0yMzNjLTRkNzMtYjM5Ni1kMzJhNDliZDVmNzEiLCJleHAiOjE2NzQ1NzkwNTMsImlhdCI6MTY3NDU3NzI1MywiaXNzIjoiQU1TIiwianRpIjoiMTU3ODM0NTEzNCJ9.K_ukp-AqISuHU-a08ewefhYd3rOVI1DxzuexpInUpOE";
        String signedAccessToken = KnoxTokenUtility.generateSignedAccessTokenJWT(new FileInputStream(cert), accessToken);


        System.out.println(signedAccessToken);
    }

}
