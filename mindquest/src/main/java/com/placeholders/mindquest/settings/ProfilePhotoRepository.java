package com.placeholders.mindquest.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProfilePhotoRepository  extends JpaRepository<ProfilePhoto, Long> {
    ProfilePhoto findById(long id);
}
