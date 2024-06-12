package birdjun.profairmanager.application.service;

import birdjun.profairmanager.application.domain.Application;
import birdjun.profairmanager.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public Application save(final Application application) {
        return applicationRepository.save(application);
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Application findById(final Long id) {
        return applicationRepository.findById(id).orElse(null);
    }
}
