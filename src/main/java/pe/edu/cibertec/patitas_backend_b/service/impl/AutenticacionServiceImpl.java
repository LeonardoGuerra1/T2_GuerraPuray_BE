package pe.edu.cibertec.patitas_backend_b.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.patitas_backend_b.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_b.service.AutenticacionService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public String[] validarUsuario(LoginRequestDTO loginRequestDTO) throws IOException {
        String[] datosUsuario = null;
        Resource resource = resourceLoader.getResource("classpath:usuarios.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (loginRequestDTO.tipoDocumento().equals(datos[0]) &&
                    loginRequestDTO.numeroDocumento().equals(datos[1]) &&
                    loginRequestDTO.password().equals(datos[2])) {
                    datosUsuario = new String[2];
                    datosUsuario[0] = datos[3];
                    datosUsuario[1] = datos[4];
                }
            }
        } catch (IOException e) {
            datosUsuario = null;
            throw new IOException(e);
        }
        return datosUsuario;
    }

    @Override
    public Date salirUsuario(LogoutRequestDTO logoutRequestDTO) throws IOException {
        Date fechaLogout = null;
        Resource resourceAuditoria = resourceLoader.getResource("classpath:auditoria.txt");
        Path rutaAuditoria = Paths.get(resourceAuditoria.getURI());

        String linea = null;
        String[] datosEncontrado = null;
        try (BufferedWriter bw = Files.newBufferedWriter(rutaAuditoria, StandardOpenOption.APPEND)) {
            Resource resourceUsuarios = resourceLoader.getResource("classpath:usuarios.txt");
            BufferedReader br = new BufferedReader(new FileReader(resourceUsuarios.getFile()));
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (logoutRequestDTO.correoUsuario().equals(datos[4])) {
                    datosEncontrado = new String[2];
                    datosEncontrado[0] = datos[0];//TIPO DOC
                    datosEncontrado[1] = datos[1];//NRO DOC
                }
            }
            if (datosEncontrado != null) {
                fechaLogout = new Date();
                StringBuilder sb = new StringBuilder();
                sb.append(datosEncontrado[0]);
                sb.append(";");
                sb.append(datosEncontrado[1]);
                sb.append(";");
                sb.append(fechaLogout);
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            fechaLogout = null;
            throw new IOException(e);
        }
        return fechaLogout;
    }
}
