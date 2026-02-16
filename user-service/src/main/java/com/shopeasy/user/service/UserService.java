package com.shopeasy.user.service;

import com.shopeasy.common.dto.AddressDTO;
import com.shopeasy.common.dto.PageInfo;
import com.shopeasy.common.dto.UserDTO;
import com.shopeasy.common.exception.ResourceNotFoundException;
import com.shopeasy.user.entity.User;
import com.shopeasy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        log.info("Fetching all users");
        return userRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return toDTO(user);
    }

    public PageInfo createPageInfo(Page<?> page) {
        return PageInfo.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .active(user.isActive())
                .addresses(user.getAddresses().stream()
                        .filter(a -> !a.isDeleted())
                        .map(a -> AddressDTO.builder()
                                .id(a.getId())
                                .street(a.getStreet())
                                .city(a.getCity())
                                .state(a.getState())
                                .zipCode(a.getZipCode())
                                .country(a.getCountry())
                                .defaultAddress(a.isDefaultAddress())
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
