package com.example.obligatorio_arbol9.controller;

import com.example.obligatorio_arbol9.dto.*;
import com.example.obligatorio_arbol9.entity.User;
import com.example.obligatorio_arbol9.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. Alta de usuario
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return ResponseEntity.ok(user);
    }

    // 2. Completar datos del usuario
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 3. Registro de familiares
    @PostMapping("/{userId}/family")
    public ResponseEntity<User> addFamilyMember(
            @PathVariable Long userId,
            @Valid @RequestBody FamilyMemberRequest request) {
        userService.addFamilyMember(userId, request.getFamilyMember(), request.getRelationship());
        User tree = userService.getGenealogyTree(userId);
        return ResponseEntity.ok(tree);
    }

    // 4. Añadir cónyuge
    @PostMapping("/{userId}/spouse")
    public ResponseEntity<User> addSpouse(
            @PathVariable Long userId,
            @Valid @RequestBody UserDTO spouseDTO) {
        userService.addSpouse(userId, spouseDTO);
        User tree = userService.getGenealogyTree(userId);
        return ResponseEntity.ok(tree);
    }

    // 5. Obtener árbol genealógico
    @GetMapping("/{userId}/tree")
    public ResponseEntity<User> getGenealogyTree(@PathVariable Long userId) {
        User tree = userService.getGenealogyTree(userId);
        return ResponseEntity.ok(tree);
    }

    // 6. Confirmar registro de usuario
    @PostMapping("/{userId}/confirm")
    public ResponseEntity<?> confirmUser(
            @PathVariable Long userId,
            @Valid @RequestBody ConfirmationRequest request) {
        userService.confirmUser(userId, request);
        return ResponseEntity.ok("Confirmación realizada exitosamente.");
    }

    // 7. Borrar usuario
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //8. Endpoint para obtener todos los usuarios sin relaciones
    @GetMapping
    public ResponseEntity<List<UserSummaryDTO>> getAllUsers() {
        List<UserSummaryDTO> users = userService.getAllUsersSummary();
        return ResponseEntity.ok(users);
    }

    //Obtener árbol genealógico por nombre de usuario
    @GetMapping("/name/{nombre}/tree")
    public ResponseEntity<User> getGenealogyTreeByName(@PathVariable String nombre) {
        User tree = userService.getGenealogyTreeByName(nombre);
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/{userId}/same-generation")
    public ResponseEntity<List<UserSummaryDTO>> getSameGenerationFamily(@PathVariable Long userId) {
        List<UserSummaryDTO> family = userService.getSameGenerationFamily(userId);
        return ResponseEntity.ok(family);
    }


    @GetMapping("/{userId}/tree-generation")
    public ResponseEntity<UserTreeDTO> getGenealogyTree(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int depth) {
        UserTreeDTO tree = userService.getGenealogyTree(userId, depth);
        return ResponseEntity.ok(tree);
    }

    //confirmaciones pendientes
    @GetMapping("/{userId}/pending-confirmations")
    public ResponseEntity<List<PendingConfirmationDTO>> getPendingConfirmations(@PathVariable Long userId) {
        List<PendingConfirmationDTO> pendingConfirmations = userService.getPendingConfirmations(userId);
        return ResponseEntity.ok(pendingConfirmations);
    }

    @GetMapping("/{userId}/kinship")
    public ResponseEntity<String> getKinship(
            @PathVariable Long userId,
            @RequestParam String targetName) {
        String kinship = userService.findKinship(userId, targetName);
        return ResponseEntity.ok(kinship);
    }

}