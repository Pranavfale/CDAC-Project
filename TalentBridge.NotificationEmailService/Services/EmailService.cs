using TalentBridge.NotificationEmailService.DTOs;
using MailKit.Net.Smtp;
using MailKit.Security;
using MimeKit;
using Microsoft.Extensions.Options;
using TalentBridge.NotificationEmailService.Models;
namespace TalentBridge.NotificationEmailService.Services;

public class EmailService : IEmailService
{
    private readonly EmailSettings emailSettings;


    public EmailService(
        IOptions<EmailSettings> options)
    {
        emailSettings = options.Value;
    }


    public async Task SendEmailAsync(EmailRequest request)
    {

        var email = new MimeMessage();

        email.From.Add(
            new MailboxAddress(
                "TalentBridge",
                emailSettings.Username
            )
        );

        email.To.Add(
            MailboxAddress.Parse(request.To)
        );

        email.Subject = request.Subject;


        email.Body = new TextPart("plain")
        {
            Text = request.Body
        };


        using var smtp = new SmtpClient();


        await smtp.ConnectAsync(
            emailSettings.Host,
            emailSettings.Port,
            SecureSocketOptions.StartTls
        );


        await smtp.AuthenticateAsync(
            emailSettings.Username,
            emailSettings.Password
        );


        await smtp.SendAsync(email);


        await smtp.DisconnectAsync(true);
    }
}