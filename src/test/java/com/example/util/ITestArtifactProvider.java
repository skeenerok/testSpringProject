package com.example.util;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface ITestArtifactProvider {

    List<Map<String, BufferedImage>> getArtifacts();
}
