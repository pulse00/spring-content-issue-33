package child;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories(basePackageClasses = FileRepository.class)
@EnableFilesystemStores(basePackageClasses = FileContentStore.class)
@EntityScan(basePackageClasses = File.class)
class FileConfig {


}