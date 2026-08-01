using Microsoft.AspNetCore.Mvc;
using TalentBridge.NotificationEmailService.DTOs;
using TalentBridge.NotificationEmailService.Services;

namespace TalentBridge.NotificationEmailService.Controllers;


[ApiController]
[Route("api/notification")]
public class NotificationController : ControllerBase
{

    private readonly IEmailService emailService;


    public NotificationController(IEmailService emailService)
    {
        this.emailService = emailService;
    }


    [HttpPost("send")]
    public async Task<IActionResult> SendEmail(
        [FromBody] EmailRequest request)
    {

        await emailService.SendEmailAsync(request);

        return Ok(new
        {
            message = "Email notification sent successfully"
        });
    }

}