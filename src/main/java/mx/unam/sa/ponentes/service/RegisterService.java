package mx.unam.sa.ponentes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.unam.sa.ponentes.models.Register;
import mx.unam.sa.ponentes.repository.RegisterRepository;


@Service
public class RegisterService {
    @Autowired
    private RegisterRepository registerRepository;

    public void saveRegister(String username, String ip, String event) {
        if (event.length() > 65535) {
            event = event.substring(0, 65535);
        }
        registerRepository.save(new Register(username, ip, event));
    }
}
