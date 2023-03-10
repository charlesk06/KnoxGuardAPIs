import com.samsung.knoxwsm.util.KnoxTokenUtility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String clientId = "eyJhbGciOiJIUzUxMiJ9.eyJjbGllbnRJZGVudGlmaWVyIjoiMjRiZDJlZTItYjFmNC00NDI0LThmYjktNWNkZGJmODM1OWZlN2UzZDVjMzUtMzk4ZS00NjkzLThhNTMtNmM0OWU2ODNiYzkxIiwiYXR0cjEiOiIxIn0.r91KVxyYMmFSSS9fVT05pQTVpiQhNMzz-86SX98BHDvFgCjVQZWu1bNJIcYjhkO2mFsLeQd6RiJHDrg_cTKu1g";
        String cert = "src/test/resources/keys.json";
        String signedClientId = null;
        try {
            signedClientId = KnoxTokenUtility.generateSignedClientIdentifierJWT(new FileInputStream(cert), clientId);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(signedClientId);
    }
}