package com.elearning.common.audit;

import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void log(String action, String entityType, Long entityId, String details) {
        try {
            AuthenticatedUser user = SecurityUtils.getCurrentUser();
            auditRepository.save(new AuditLog(user.id(), action, entityType, entityId, details, null));
        } catch (Exception e) {
            auditRepository.save(new AuditLog(null, action, entityType, entityId, details, null));
        }
    }
}
