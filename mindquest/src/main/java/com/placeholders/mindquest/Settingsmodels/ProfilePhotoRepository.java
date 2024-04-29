package com.placeholders.mindquest.Settingsmodels;

import com.placeholders.mindquest.role_utils.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProfilePhotoRepository  extends JpaRepository<ProfilePhoto, Long> {
    ProfilePhoto findById(long id);
}
