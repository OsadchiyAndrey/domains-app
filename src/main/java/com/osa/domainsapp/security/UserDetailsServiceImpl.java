package com.osa.domainsapp.security;

import com.osa.domainsapp.dao.UserRepository;
import com.osa.domainsapp.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserEntity account = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("Username '%s' not found", username)));
    return SecurityAccount.fromAccount(account);
  }
}
