package cl.prezdev.config;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

import cl.prezdev.device.DeviceManager;

@Configuration
public class ShellPromptConfig {

    @Value("${prompt}")
    private String prompt;

    @Bean
    PromptProvider promptProvider(DeviceManager deviceManager) {
        return () -> {
            String formattedPrompt = String.format(prompt, deviceManager.count());
            return new AttributedString(formattedPrompt, AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN));
        };
    }
}

