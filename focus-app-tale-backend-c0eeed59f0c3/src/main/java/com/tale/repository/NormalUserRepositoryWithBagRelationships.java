package com.tale.repository;

import com.tale.domain.NormalUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface NormalUserRepositoryWithBagRelationships {
    Optional<NormalUser> fetchBagRelationships(Optional<NormalUser> normalUser);

    List<NormalUser> fetchBagRelationships(List<NormalUser> normalUsers);

    Page<NormalUser> fetchBagRelationships(Page<NormalUser> normalUsers);
}
