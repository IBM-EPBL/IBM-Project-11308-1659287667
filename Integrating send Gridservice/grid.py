import sendgrid
import os
from sendgrid.helpers.mail import Mail, Email, To, Content

sg = sendgrid.SendGridAPIClient(api_key='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')
from_email = Email("example@gmail.com")  # Change to your verified sender
to_email = To("test@gmail.com")  # Change to your recipient
subject = "Sending with SendGrid is Fun"
content = Content("text/plain", "and easy to do anywhere, even with Python")
mail = Mail(from_email, to_email, subject, content)

mail_json = mail.get()

response = sg.client.mail.send.post(request_body=mail_json)
print(response.status_code)
print(response.headers)