package com.starter.blog.controllers;

import com.starter.blog.config.JwtService;
import com.starter.blog.models.Post;
import com.starter.blog.models.Tag;
import com.starter.blog.repositories.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

/**
 * Created by HachNV on Feb 07, 2023
 */
@RestController
@RequiredArgsConstructor
public class PostsController {
//    @Value("${file-upload-path}")
    private String uploadPath;

    private final PostRepository postRepository;

    private final JwtService jwtService;

    /**
     *
     * @param model
     * @return list posts
     */
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(Model model){
        List<Post> allPost = postRepository.getAllPosts();
//        model.addAttribute("page", postRepository.getAllPosts() );
        return ResponseEntity.ok(allPost);
    }

    /**
     *
     * @param postSubmitted
     * @param validation
     * @param model
     * @param uploadedFile
     * @param tags
     * @return create post
     */
    @PostMapping("posts/create")
    public ResponseEntity<String> createPost(@Valid Post postSubmitted, Principal principal, Errors validation, Model model, @RequestParam(name = "file") MultipartFile uploadedFile, @RequestParam(name = "tags") List<Tag> tags){

        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            System.out.println(validation.getAllErrors());
            model.addAttribute("post", postSubmitted);

        }

        // Files handle
//        uploadFileHandler(postSubmitted, model, uploadedFile);

        postSubmitted.setTags(tags);
//        postSubmitted.setUser(principal.getName());
        Post savedPost = postRepository.save(postSubmitted);

        return ResponseEntity.ok("Post was created");
    }

    private void uploadFileHandler(@Valid Post postSubmitted, Model m, @RequestParam(name = "file") MultipartFile uploadedFile) {
        if(!uploadedFile.getOriginalFilename().isEmpty()){

            String filename = uploadedFile.getOriginalFilename().replace(" ", "_").toLowerCase();
            String filepath = Paths.get(uploadPath, filename).toString();
            File destinationFile = new File(filepath);

            // Try to save it in the server
            try {
                uploadedFile.transferTo(destinationFile);
                m.addAttribute("message", "File successfully uploaded!");
            } catch (IOException e) {
                e.printStackTrace();
                m.addAttribute("message", "Oops! Something went wrong! " + e);
            }

            //Save it in the DB
            postSubmitted.setImageUrl(filename);
        }
    }
}
