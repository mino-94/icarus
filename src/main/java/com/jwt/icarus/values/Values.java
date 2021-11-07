package com.jwt.icarus.values;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;

import java.util.HashMap;
import java.util.Map;

public interface Values {

    Map<String, Map> MAPPING_INFO = new HashMap<>();

    Map MEDIA_TYPES = new HashMap<>();

    JwtBuilder PUBLISH_TOKEN = Jwts.builder().compressWith(CompressionCodecs.DEFLATE);

    JwtParserBuilder SUBSCRIBE_TOKEN = Jwts.parserBuilder();

    String DEFAULT_FORMAT = "json";

}
