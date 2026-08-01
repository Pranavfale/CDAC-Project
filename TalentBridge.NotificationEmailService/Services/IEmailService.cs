using TalentBridge.NotificationEmailService.DTOs;

namespace TalentBridge.NotificationEmailService.Services;

public interface IEmailService
{
    Task SendEmailAsync(EmailRequest request);
}