package recipes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.businesslayer.User;
import recipes.persistence.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFind = userRepository.findByEmail(username);
        if (userFind == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return new UserDetailsImp(userFind);
    }
}
