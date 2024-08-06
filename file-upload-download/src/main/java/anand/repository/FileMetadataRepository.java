package anand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import anand.entity.FileMetadata;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long>{

}
