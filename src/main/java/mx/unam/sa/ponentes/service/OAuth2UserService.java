package mx.unam.sa.ponentes.service;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mx.unam.sa.ponentes.dto.AdditionalDetailsDto;
import mx.unam.sa.ponentes.dto.Oauth2UserInfoDto;
import mx.unam.sa.ponentes.entity.Authority;
import mx.unam.sa.ponentes.entity.User;
import mx.unam.sa.ponentes.entity.UserPrincipal;
import mx.unam.sa.ponentes.repository.AuthorityRepository;
import mx.unam.sa.ponentes.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    @SneakyThrows
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        log.trace("Load user {}", oAuth2UserRequest);
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        Oauth2UserInfoDto userInfoDto = Oauth2UserInfoDto
                .builder()
                .name(oAuth2User.getAttributes().get("name").toString())
                .id(oAuth2User.getAttributes().get("sub").toString())
                .email(oAuth2User.getAttributes().get("email").toString())
                .picture(oAuth2User.getAttributes().get("picture").toString())
                .build();

        log.trace("User info is {}", userInfoDto);
        Optional<User> userOptional = userRepository.findByUsername(userInfoDto.getEmail());
        log.trace("User is {}", userOptional);
        User user = userOptional
                .map(existingUser -> updateExistingUser(existingUser, userInfoDto))
                .orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, Oauth2UserInfoDto userInfoDto) {
        User user = new User();
        user.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        user.setProviderId(userInfoDto.getId());
        user.setName(userInfoDto.getName());
        user.setUsername(userInfoDto.getEmail());
        user.setPicture(userInfoDto.getPicture());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, Oauth2UserInfoDto userInfoDto) {
        existingUser.setName(userInfoDto.getName());
        existingUser.setPicture(userInfoDto.getPicture());
        return userRepository.save(existingUser);
    }

    public void completeRegistration(String username, AdditionalDetailsDto additionalDetails) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Authority authority = new Authority();
            authority.setAuthority(additionalDetails.getAuthority().getAuthority());
            authority = authorityRepository.save(authority);
            user.setAuthorities(Set.of(authority));
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
