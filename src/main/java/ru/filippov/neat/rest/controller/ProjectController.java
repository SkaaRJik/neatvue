package ru.filippov.neat.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.config.jwt.JwtProvider;
import ru.filippov.neat.domain.Project;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.parser.excel.ExcelParser;
import ru.filippov.neat.service.project.ProjectServiceImpl;
import ru.filippov.neat.service.user.UserPrincipal;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "api/projects")
public class ProjectController {



    @Bean
    public ExcelParser getExcelParser(){
        return new ExcelParser();
    }

    @Autowired
    ExcelParser excelParser;

    @Autowired
    JwtProvider tokenProvider;

    @Autowired
    ProjectServiceImpl projectService;

    @PostMapping("/parse")
    public ResponseEntity<?> parseExcel(@RequestBody MultipartFile file){

        try {
            return ResponseEntity.ok(excelParser.parseFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("CANT_PROCESS_FILE", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProject(@AuthenticationPrincipal UserPrincipal user, @RequestBody Map<String,Object> params){



        Project project = null;
        try{
            project = projectService.saveProject(user.toUser(), params);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>("CANT_SAVE_FILE", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<Long>(project.getId(), HttpStatus.OK);

    }
}
