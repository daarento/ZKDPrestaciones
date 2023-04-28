package com.springzk.pruebazk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class ZkdPrestacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZkdPrestacionesApplication.class, args);
	}

	@GetMapping("/formulario")
	public String formulario() {
		return "formulario";
	}
}
