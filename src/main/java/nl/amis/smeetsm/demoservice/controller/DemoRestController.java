package nl.amis.smeetsm.demoservice.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoRestController.
 */
@RestController
public class DemoRestController {
    /**
     * @return Hi there.
     */
    @GetMapping("/rest/demo")
    public String demoReply() {
        return (new StringBuilder().append("Hi there")).toString();
    }

    @GetMapping("/rest/hello")
    public String hello(@RequestParam String name){
        return "Hello " + name;
    }

    @GetMapping("/rest/ping")
    public String ping(@RequestParam String ipAddress){
        String response = "";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "ping -c 4 "+ipAddress);
        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response = response + line + "\n";
            }

            int exitCode = process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

}
