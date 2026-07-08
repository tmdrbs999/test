import kr.go.iop.cm.module.utils.CryptoConfig;
import org.egovframe.rte.fdl.cryptography.EgovEnvCryptoService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/////변경사항ㅇ아앙아아아앙

// 위키 "아래 코드 실행" 원문 그대로.
// files/*.properties 의 각 KEY=값 을 암호화하여 result.yaml 로 출력한다.
public class ENC {

    public static void main(String[] args) throws Exception {
        CryptoConfig cryptoConfig = new CryptoConfig();
        EgovEnvCryptoService service = cryptoConfig.cryptoService();

        java.io.PrintStream con = System.out;
        Path outFile = Paths.get("result.yaml").toAbsolutePath();
        System.setOut(new java.io.PrintStream("result.yaml", "UTF-8"));

        List<Path> files = new ArrayList<>();
        for (String s : (args.length == 0 ? new String[]{"files"} : args)) {
            Path p = Paths.get(s);
            if (Files.isDirectory(p)) {
                try (var st = Files.list(p)) {
                    st.filter(f -> f.toString().endsWith(".properties")).sorted().forEach(files::add);
                }
            } else files.add(p);
        }

        for (Path f : files) {
            System.out.println("# ====== " + f.getFileName() + "========");
            for (String line : Files.readAllLines(f)) {
                String s = line.strip();
                if (s.isEmpty()) continue;
                if (s.isEmpty() || s.startsWith("#")) { System.out.println(" " + s); continue; }
                int i = s.indexOf("=");
                if (i < 0) continue;
                System.out.println("  - name: " + s.substring(0, i).strip());
                System.out.println("    value: ENC(" + service.encrypt(s.substring(i + 1).strip()) + ")");
            }
            System.out.println();
        }

        System.out.flush();
        con.println("[저장 경로] => " + outFile);
    }
}
