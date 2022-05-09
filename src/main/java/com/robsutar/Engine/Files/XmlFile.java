package com.robsutar.Engine.Files;

import com.robsutar.Engine.Helpers.FileManager;
import org.w3c.dom.Document;

import java.io.File;

public class XmlFile extends AssetsFile{
    public final Document xml;
    public XmlFile(File file) {
        super(file);
        xml = FileManager.loadXml(file);
    }
}
