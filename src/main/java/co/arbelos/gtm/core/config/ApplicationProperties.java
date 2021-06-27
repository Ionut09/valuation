package co.arbelos.gtm.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Value("${valuation.ingestor.ftpUrl}")
    public String ftpUrl;
    @Value("${valuation.ingestor.ftpUser}")
    public String ftpUser;
    @Value("${valuation.ingestor.ftpPasswd}")
    public String ftpPasswd;
    @Value("${valuation.ingestor.ftpFrom}")
    public String ftpFrom;
    @Value("${valuation.ingestor.ftpTo}")
    public String ftpTo;

    public ApplicationProperties() {
    }

    public String getFtpUrl() {
        return this.ftpUrl;
    }

    public String getFtpUser() {
        return this.ftpUser;
    }

    public String getFtpPasswd() {
        return this.ftpPasswd;
    }

    public String getFtpFrom() {
        return this.ftpFrom;
    }

    public String getFtpTo() {
        return this.ftpTo;
    }

    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public void setFtpPasswd(String ftpPasswd) {
        this.ftpPasswd = ftpPasswd;
    }

    public void setFtpFrom(String ftpFrom) {
        this.ftpFrom = ftpFrom;
    }

    public void setFtpTo(String ftpTo) {
        this.ftpTo = ftpTo;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ApplicationProperties)) return false;
        final ApplicationProperties other = (ApplicationProperties) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ftpUrl = this.getFtpUrl();
        final Object other$ftpUrl = other.getFtpUrl();
        if (this$ftpUrl == null ? other$ftpUrl != null : !this$ftpUrl.equals(other$ftpUrl)) return false;
        final Object this$ftpUser = this.getFtpUser();
        final Object other$ftpUser = other.getFtpUser();
        if (this$ftpUser == null ? other$ftpUser != null : !this$ftpUser.equals(other$ftpUser)) return false;
        final Object this$ftpPasswd = this.getFtpPasswd();
        final Object other$ftpPasswd = other.getFtpPasswd();
        if (this$ftpPasswd == null ? other$ftpPasswd != null : !this$ftpPasswd.equals(other$ftpPasswd)) return false;
        final Object this$ftpFrom = this.getFtpFrom();
        final Object other$ftpFrom = other.getFtpFrom();
        if (this$ftpFrom == null ? other$ftpFrom != null : !this$ftpFrom.equals(other$ftpFrom)) return false;
        final Object this$ftpTo = this.getFtpTo();
        final Object other$ftpTo = other.getFtpTo();
        if (this$ftpTo == null ? other$ftpTo != null : !this$ftpTo.equals(other$ftpTo)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ApplicationProperties;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ftpUrl = this.getFtpUrl();
        result = result * PRIME + ($ftpUrl == null ? 43 : $ftpUrl.hashCode());
        final Object $ftpUser = this.getFtpUser();
        result = result * PRIME + ($ftpUser == null ? 43 : $ftpUser.hashCode());
        final Object $ftpPasswd = this.getFtpPasswd();
        result = result * PRIME + ($ftpPasswd == null ? 43 : $ftpPasswd.hashCode());
        final Object $ftpFrom = this.getFtpFrom();
        result = result * PRIME + ($ftpFrom == null ? 43 : $ftpFrom.hashCode());
        final Object $ftpTo = this.getFtpTo();
        result = result * PRIME + ($ftpTo == null ? 43 : $ftpTo.hashCode());
        return result;
    }

    public String toString() {
        return "ApplicationProperties(ftpUrl=" + this.getFtpUrl() + ", ftpUser=" + this.getFtpUser() + ", ftpPasswd=" + this.getFtpPasswd() + ", ftpFrom=" + this.getFtpFrom() + ", ftpTo=" + this.getFtpTo() + ")";
    }
}
