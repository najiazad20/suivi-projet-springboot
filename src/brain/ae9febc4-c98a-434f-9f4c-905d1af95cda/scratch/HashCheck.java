import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashCheck {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String[] hashes = {
            "$2a$10$7SULBYD9po2IFrQysUiykubkcd/NgDj2OhKzJ2PKRhWLYLwUIRC3a",
            "$2a$10$p5kcUih7kOeEMIEfktNT8uva7DClWyMf4RY5CwQp1YaX7cbqWOvYi",
            "$2a$10$LVjMiDkuBCp0b83PFqlPFe34HkZ/EFLoWuxkWs19jxlVVUYH8daj6",
            "$2a$10$80bDPR20szKth7AJkCSwo.YT0krABFi.MaUCi.ZNjIoZCUcqJ1nn2"
        };
        String[] common = {"password", "123456", "admin", "najia", "khadija", "ait_dir", "kaoutar", "azi", "ait_sec"};
        
        for (String h : hashes) {
            System.out.println("Checking hash: " + h);
            for (String c : common) {
                if (encoder.matches(c, h)) {
                    System.out.println("  FOUND MATCH: " + c);
                }
            }
        }
    }
}
