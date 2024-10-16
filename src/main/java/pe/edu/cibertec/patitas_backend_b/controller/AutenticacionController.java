package pe.edu.cibertec.patitas_backend_b.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.patitas_backend_b.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_backend_b.service.AutenticacionService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/autenticacion")
@CrossOrigin(origins = "http://localhost:5173")
public class AutenticacionController {

    @Autowired
    AutenticacionService autenticacionService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO){
        try {
            Thread.sleep(Duration.ofSeconds(3));
            String[] datosUsuario = autenticacionService.validarUsuario(loginRequestDTO);

            if (datosUsuario == null)
                return new LoginResponseDTO("01", "Error: Usuario no encontrado", "", "");
            else {
                System.out.println("Respuesta backend: " + Arrays.toString(datosUsuario));
                return new LoginResponseDTO("00", "", datosUsuario[0], datosUsuario[1]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponseDTO("99", "Error: Ocurrió un problema", "", "");
        }
    }

    @PostMapping("/logout")
    public LogoutResponseDTO logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        try {
            Thread.sleep(Duration.ofSeconds(3));
            Date fechaLogout = autenticacionService.salirUsuario(logoutRequestDTO);
            System.out.println("Respuesta backend: " + fechaLogout);

            if (fechaLogout == null)
                return new LogoutResponseDTO(false, null, "Error: No se pudo registrar auditoría");
            else
                return new LogoutResponseDTO(true, fechaLogout, "");
        } catch (Exception e) {
            return new LogoutResponseDTO(false, null, "Error: Ocurrió un problema");
        }
    }
}
