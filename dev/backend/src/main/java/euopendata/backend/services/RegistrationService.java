package euopendata.backend.services;

import euopendata.backend.email.EmailSender;
import euopendata.backend.email.EmailService;
import euopendata.backend.models.ConfirmationToken;
import euopendata.backend.models.Users;
import euopendata.backend.models.requests.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UsersService usersService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    public ResponseEntity<String> register(RegistrationRequest request) {

        if(!isEmailValid(request.getEmail()))
            throw new RuntimeException("Email is not valid!");

        ResponseEntity<String> token = usersService.signUpUser(
                new Users(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getUsername(),
                        request.getPassword()
                )
        );
        if(token.getStatusCode().is2xxSuccessful()) {
            String link = "http://54.167.96.255:8081/auth/confirm?token=" + token.getBody();
            emailSender.send(request.getEmail(),
                   buildEmail(request.getFirstName(), link)
            );
        }
        return token;
    }

    public boolean isEmailValid(String email)
    {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Transactional
    public String confirmToken(String token)
    {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(()->
                    new IllegalStateException("token not found"));
        if(confirmationToken.getConfirmedAt() !=null)
            throw new IllegalStateException("email already confirmed");
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now()))
            throw new IllegalStateException("token expired");

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        usersService.enableUser(
                confirmationToken.getUser().getEmail()
        );
        System.out.println(confirmationToken.getUser().getEmail());
        return "confirmed";
    }
    private String buildEmail(String name, String link) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">" +
                "" +
                "<head>" +
                "  <!--[if gte mso 9]>" +
                "<xml>" +
                "  <o:OfficeDocumentSettings>" +
                "    <o:AllowPNG/>" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>" +
                "  </o:OfficeDocumentSettings>" +
                "</xml>" +
                "<![endif]-->" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "  <meta name=\"x-apple-disable-message-reformatting\">" +
                "  <!--[if !mso]><!-->" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "  <!--<![endif]-->" +
                "  <title></title>" +
                "" +
                "  <style type=\"text/css\">" +
                "    @media only screen and (min-width: 620px) {" +
                "      .u-row {" +
                "        width: 600px !important;" +
                "      }" +
                "      .u-row .u-col {" +
                "        vertical-align: top;" +
                "      }" +
                "      .u-row .u-col-100 {" +
                "        width: 600px !important;" +
                "      }" +
                "    }" +
                "    " +
                "    @media (max-width: 620px) {" +
                "      .u-row-container {" +
                "        max-width: 100% !important;" +
                "        padding-left: 0px !important;" +
                "        padding-right: 0px !important;" +
                "      }" +
                "      .u-row .u-col {" +
                "        min-width: 320px !important;" +
                "        max-width: 100% !important;" +
                "        display: block !important;" +
                "      }" +
                "      .u-row {" +
                "        width: 100% !important;" +
                "      }" +
                "      .u-col {" +
                "        width: 100% !important;" +
                "      }" +
                "      .u-col>div {" +
                "        margin: 0 auto;" +
                "      }" +
                "    }" +
                "    " +
                "    body {" +
                "      margin: 0;" +
                "      padding: 0;" +
                "    }" +
                "    " +
                "    table," +
                "    tr," +
                "    td {" +
                "      vertical-align: top;" +
                "      border-collapse: collapse;" +
                "    }" +
                "    " +
                "    p {" +
                "      margin: 0;" +
                "    }" +
                "    " +
                "    .ie-container table," +
                "    .mso-container table {" +
                "      table-layout: fixed;" +
                "    }" +
                "    " +
                "    * {" +
                "      line-height: inherit;" +
                "    }" +
                "    " +
                "    a[x-apple-data-detectors='true'] {" +
                "      color: inherit !important;" +
                "      text-decoration: none !important;" +
                "    }" +
                "    " +
                "    table," +
                "    td {" +
                "      color: #000000;" +
                "    }" +
                "    " +
                "    #u_body a {" +
                "      color: #0000ee;" +
                "      text-decoration: underline;" +
                "    }" +
                "    " +
                "    @media (max-width: 480px) {" +
                "      #u_content_image_4 .v-src-width {" +
                "        width: auto !important;" +
                "      }" +
                "      #u_content_image_4 .v-src-max-width {" +
                "        max-width: 68% !important;" +
                "      }" +
                "    }" +
                "  </style>"+
                "<!--[if !mso]><!-->"+
                "  <link href=\"https://fonts.googleapis.com/css?family=Cabin:400,700\" rel=\"stylesheet\" type=\"text/css\">"+
                "  <!--<![endif]-->"+
                ""+
                "</head>"+
                ""+
                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #f9f9f9;color: #000000\">"+
                "  <!--[if IE]><div class=\"ie-container\"><![endif]-->"+
                "  <!--[if mso]><div class=\"mso-container\"><![endif]-->"+
                "  <table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9f9;width:100%\" cellpadding=\"0\" cellspacing=\"0\">"+
                "    <tbody>"+
                "      <tr style=\"vertical-align: top\">"+
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">"+
                "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #f9f9f9;\"><![endif]-->"+
                ""+
                ""+
                ""+
                "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"+
                "            <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">"+
                "              <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"+
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->"+
                ""+
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->"+
                "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"+
                "                  <div style=\"height: 100%;width: 100% !important;\">"+
                "                    <!--[if (!mso)&(!IE)]><!-->"+
                "                    <div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">"+
                "                      <!--<![endif]-->"+
                ""+
                "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <div style=\"font-size: 14px; color: #afb0c7; line-height: 170%; text-align: center; word-wrap: break-word;\">"+
                "                                <p style=\"font-size: 14px; line-height: 170%;\"><span style=\"font-size: 14px; line-height: 23.8px;\">View Email in Browser</span></p>"+
                "                              </div>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <!--[if (!mso)&(!IE)]><!-->"+
                "                    </div>"+
                "                    <!--<![endif]-->"+
                "                  </div>"+
                "                </div>"+
                "                <!--[if (mso)|(IE)]></td><![endif]-->"+
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"+
                "              </div>"+
                "            </div>"+
                "          </div>"+
                ""+
                ""+
                ""+
                ""+
                "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"+
                "            <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">"+
                "              <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"+
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->"+
                ""+
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->"+
                "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"+
                "                  <div style=\"height: 100%;width: 100% !important;\">"+
                "                    <!--[if (!mso)&(!IE)]><!-->"+
                "                    <div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">"+
                "                      <!--<![endif]-->"+
                ""+
                "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">"+
                "                                <tr>"+
                "                                  <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">"+
                ""+
                "                                    <img align=\"center\" border=\"0\" src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/255px-Flag_of_Europe.svg.png\" alt=\"\" title=\"\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 12%;max-width: 69.6px;\""+
                "                                      width=\"69.6\" class=\"v-src-width v-src-max-width\" />"+
                ""+
                "                                  </td>"+
                "                                </tr>"+
                "                              </table>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <table id=\"u_content_image_4\" style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">"+
                "                                <tr>"+
                "                                  <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">"+
                ""+
                "                                    <img align=\"center\" border=\"0\" src=\"https://assets.unlayer.com/projects/229494/1713982081181-email%20photo.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 44%;max-width: 246.4px;\""+
                "                                      width=\"246.4\" class=\"v-src-width v-src-max-width\" />"+
                ""+
                "                                  </td>"+
                "                                </tr>"+
                "                              </table>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <!--[if (!mso)&(!IE)]><!-->"+
                "                    </div>"+
                "                    <!--<![endif]-->"+
                "                  </div>"+
                "                </div>"+
                "                <!--[if (mso)|(IE)]></td><![endif]-->"+
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"+
                "              </div>"+
                "            </div>"+
                "          </div>"+
                ""+
                ""+
                ""+
                ""+
                "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"+
                "            <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #003399;\">"+
                "              <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"+
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #003399;\"><![endif]-->"+
                ""+
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->"+
                "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"+
                "                  <div style=\"height: 100%;width: 100% !important;\">"+
                "                    <!--[if (!mso)&(!IE)]><!-->"+
                "                    <div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">"+
                "                      <!--<![endif]-->"+
                ""+
                "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 10px 10px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">"+
                "                                <tr>"+
                "                                  <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">"+
                ""+
                "                                    <img align=\"center\" border=\"0\" src=\"https://cdn.templates.unlayer.com/assets/1597218650916-xxxxc.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 26%;max-width: 150.8px;\""+
                "                                      width=\"150.8\" class=\"v-src-width v-src-max-width\" />"+
                ""+
                "                                  </td>"+
                "                                </tr>"+
                "                              </table>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                "<table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <div style=\"font-size: 14px; color: #e5eaf5; line-height: 140%; text-align: center; word-wrap: break-word;\">"+
                "                                <p style=\"font-size: 14px; line-height: 140%;\"><strong>THANK YOU FOR REGISTERING!</strong></p>"+
                "                              </div>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 10px 31px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <div style=\"font-size: 14px; color: #e5eaf5; line-height: 140%; text-align: center; word-wrap: break-word;\">"+
                "                                <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 28px; line-height: 39.2px;\"><strong><span style=\"line-height: 39.2px; font-size: 28px;\">Verify Your E-mail Address </span></strong>"+
                "                                  </span>"+
                "                                </p>"+
                "                              </div>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <!--[if (!mso)&(!IE)]><!-->"+
                "                    </div>"+
                "                    <!--<![endif]-->"+
                "                  </div>"+
                "                </div>"+
                "                <!--[if (mso)|(IE)]></td><![endif]-->"+
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"+
                "              </div>"+
                "            </div>"+
                "          </div>"+
                ""+
                ""+
                ""+
                ""+
                "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"+
                "            <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">"+
                "              <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"+
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->"+
                ""+
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->"+
                "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"+
                "                  <div style=\"height: 100%;width: 100% !important;\">"+
                "                    <!--[if (!mso)&(!IE)]><!-->"+
                "                    <div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">"+
                "                      <!--<![endif]-->"+
                ""+
                "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <div style=\"font-size: 14px; line-height: 160%; text-align: center; word-wrap: break-word;\">"+
                "                                <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 22px; line-height: 35.2px;\">Hello, </span></p>"+
                "                                <p style=\"font-size: 14px; line-height: 160%;\"><span style=\"font-size: 18px; line-height: 28.8px;\">You're almost ready to get started. Please click on the button below to verify your email address and enjoy browsing information about your favorite locations! </span></p>"+
                "                              </div>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->"+
                "                              <div align=\"center\">"+
                "                                <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"https://ro.wikipedia.org/wiki/Pagina_principal%C4%83\" style=\"height:46px; v-text-anchor:middle; width:235px;\" arcsize=\"8.5%\"  stroke=\"f\" fillcolor=\"#f86807\"><w:anchorlock/><center style=\"color:#FFFFFF;\"><![endif]-->"+
                "                                <a href=\""+link+ "\" target=\"_blank\" class=\"v-button\" style=\"box-sizing: border-box;display: inline-block;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #f86807; border-radius: 4px;-webkit-border-radius: 4px; -moz-border-radius: 4px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">"+
                "                                  <span style=\"display:block;padding:14px 44px 13px;line-height:120%;\"><span style=\"font-size: 16px; line-height: 19.2px;\"><strong><span style=\"line-height: 19.2px; font-size: 16px;\">VERIFY YOUR EMAIL</span></strong>"+
                "                                  </span>"+
                "                                  </span>"+
                "                                </a>"+
                "                                <!--[if mso]></center></v:roundrect><![endif]-->"+
                "                              </div>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                "<table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px 60px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <div style=\"font-size: 14px; line-height: 160%; text-align: center; word-wrap: break-word;\">"+
                "                                <p style=\"line-height: 160%; font-size: 14px;\"><span style=\"font-size: 18px; line-height: 28.8px;\">Thanks,</span></p>"+
                "                                <p style=\"line-height: 160%; font-size: 14px;\"><span style=\"font-size: 18px; line-height: 28.8px;\">The EU Open Data Team</span></p>"+
                "                              </div>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <!--[if (!mso)&(!IE)]><!-->"+
                "                    </div>"+
                "                    <!--<![endif]-->"+
                "                  </div>"+
                "                </div>"+
                "                <!--[if (mso)|(IE)]></td><![endif]-->"+
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"+
                "              </div>"+
                "            </div>"+
                "          </div>"+
                ""+
                ""+
                ""+
                ""+
                "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">"+
                "            <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #003399;\">"+
                "              <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">"+
                "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #003399;\"><![endif]-->"+
                ""+
                "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->"+
                "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">"+
                "                  <div style=\"height: 100%;width: 100% !important;\">"+
                "                    <!--[if (!mso)&(!IE)]><!-->"+
                "                    <div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">"+
                "                      <!--<![endif]-->"+
                ""+
                "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">"+
                "                        <tbody>"+
                "                          <tr>"+
                "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">"+
                ""+
                "                              <div style=\"font-size: 14px; color: #fafafa; line-height: 180%; text-align: center; word-wrap: break-word;\">"+
                "                                <p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 16px; line-height: 28.8px;\">Copyrights © EUOpenData All Rights Reserved</span></p>"+
                "                              </div>"+
                ""+
                "                            </td>"+
                "                          </tr>"+
                "                        </tbody>"+
                "                      </table>"+
                ""+
                "                      <!--[if (!mso)&(!IE)]><!-->"+
                "                    </div>"+
                "                    <!--<![endif]-->"+
                "                  </div>"+
                "                </div>"+
                "                <!--[if (mso)|(IE)]></td><![endif]-->"+
                "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->"+
                "              </div>"+
                "            </div>"+
                "          </div>"+
                ""+
                ""+
                ""+
                ""+
                "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->"+
                "        </td>"+
                "      </tr>"+
                "    </tbody>"+
                "  </table>"+
                "  <!--[if mso]></div><![endif]-->"+
                "  <!--[if IE]></div><![endif]-->"+
                "</body>"+
                ""+
                "</html>";
    }


}
