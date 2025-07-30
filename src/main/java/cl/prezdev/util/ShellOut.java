package cl.prezdev.util;

import org.jline.terminal.Terminal;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ShellOut {

    private final Terminal terminal;

    public void print(String message) {
        terminal.writer().println(message);
        terminal.writer().flush();
    }
    
}
