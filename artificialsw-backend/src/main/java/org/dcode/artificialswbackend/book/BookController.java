package org.dcode.artificialswbackend.book;

import org.dcode.artificialswbackend.book.dto.FlowerBookResponseDto;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;
    private final JwtUtil jwtUtil;

    public BookController(BookService bookService, JwtUtil jwtUtil) {
        this.bookService = bookService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/book/flower")
    public ResponseEntity<List<FlowerBookResponseDto>> getFlowerBook(@RequestHeader("Authorization") String authHeader) {
        try {
            // JWT 토큰에서 Bearer 제거
            String token = authHeader.replace("Bearer ", "");
            
            // 토큰에서 familyId 추출
            Long familyId = jwtUtil.validateAndGetFamilyId(token);
            
            // 꽃 도감 조회
            List<FlowerBookResponseDto> flowerBook = bookService.getFlowerBook(familyId);
            
            return ResponseEntity.ok(flowerBook);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}