package de.stealwonders.epicsuite.resourcepack;

public class ResourcePack {

    private final String key;
    private String url;
    private String hash;

    public ResourcePack(final String key, final String url, final String hash) {
        this.key = key;
        this.url = url;
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }

}
