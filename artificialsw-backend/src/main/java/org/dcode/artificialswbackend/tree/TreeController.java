package org.dcode.artificialswbackend.tree;

import org.dcode.artificialswbackend.tree.dto.TreeFlowerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping("/tree/custom/{treeId}/flower")
    public ResponseEntity<List<TreeFlowerResponseDto>> getTreeFlowers(@PathVariable Long treeId) {
        try {
            List<TreeFlowerResponseDto> flowers = treeService.getTreeFlowers(treeId);
            return ResponseEntity.ok(flowers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}