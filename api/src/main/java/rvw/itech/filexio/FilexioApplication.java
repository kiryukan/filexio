package rvw.itech.filexio;

import java.security.SecureRandom;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import rvw.itech.filexio.security.WebSecurityConfig;
import rvw.itech.filexio.storage.StorageService;
import rvw.itech.filexio.utility.UtilityService;

@Import({ WebSecurityConfig.class })
@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class })//(exclude = { SecurityAutoConfiguration.class }) // !! decomment to disable security (cf application properties)
public class FilexioApplication /*implements CommandLineRunner*/{
    
    @Resource
    StorageService storageService;
    public static void main(String[] args) {
        SpringApplication.run(FilexioApplication.class, args);
        System.out.println(UtilityService.testStaticFct());
        String easy = "0123456789ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
        System.out.println(UtilityService.generateRootFolderName(8, new SecureRandom(), easy));
    }
    
    /*@Override
    public void run(String... arg) throws Exception {
      storageService.deleteAll();
      storageService.init();
    }*/
}
