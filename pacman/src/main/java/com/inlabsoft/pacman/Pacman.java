package com.inlabsoft.pacman;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main application class for Pacman.
 *
 * To run: mvn spring-boot:run (if using Spring Boot)
 * Or as standalone JAR: java -jar target/pacman-1.0.0-SNAPSHOT.jar
 */
@Command(name = "filepacker",
mixinStandardHelpOptions = true,
version = "2.0",
description = "Splits, obfuscates, and reassembles binary files using Markdown containers with dynamic sizes.",
subcommands = { Pacman.PackCommand.class, Pacman.UnpackCommand.class })
public final class Pacman implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(Pacman.class);


	    public static void main(String[] args) {
	        int exitCode = new CommandLine(new Pacman()).execute(args);
	        System.exit(exitCode);
	    }

	    @Override
	    public void run() {
	        CommandLine.usage(this, System.out);
	    }

	    // --- PACK COMMAND ---
	    @Command(name = "pack", description = "Splits a binary file into obfuscated Markdown packages.")
	    static class PackCommand implements Runnable {
	        @Parameters(index = "0", description = "The source binary file to pack.")
	        private File sourceFile;

	        @Option(names = {"-o", "--output-dir"}, description = "Directory to save MD files.", defaultValue = ".")
	        private File outputDir;

	        @Option(names = {"-k", "--key"}, description = "XOR key byte for obfuscation.", defaultValue = "42")
	        private byte xorKey;

	        // Новая опция для настройки размера чанка в КБ
	        @Option(names = {"-s", "--chunk-size"}, description = "Size of each chunk in Kilobytes.", defaultValue = "10")
	        private int chunkSizeKb;

	        // Новая опция для настройки лимита чанков в одном MD файле
	        @Option(names = {"-c", "--chunks-per-file"}, description = "Maximum number of chunks per Markdown file.", defaultValue = "10")
	        private int chunksPerFile;

	        @Override
	        public void run() {
	            if (!sourceFile.exists() || !sourceFile.isFile()) {
	                System.err.println("Error: Source file does not exist.");
	                return;
	            }

	            if (chunkSizeKb <= 0 || chunksPerFile <= 0) {
	                System.err.println("Error: Chunk size and chunks per file must be greater than 0.");
	                return;
	            }

	            int actualChunkSizeBytes = chunkSizeKb * 1024;

	            try {
	                if (!outputDir.exists()) {
	                    Files.createDirectories(outputDir.toPath());
	                }

	                try (InputStream is = new BufferedInputStream(new FileInputStream(sourceFile))) {
	                    byte[] buffer = new byte[actualChunkSizeBytes];
	                    int bytesRead;
	                    int chunkCounter = 1;
	                    int packageCounter = 1;
	                    List<String> currentPackageChunks = new ArrayList<>();

	                    while ((bytesRead = is.read(buffer)) != -1) {
	                        byte[] actualChunk = (bytesRead == actualChunkSizeBytes) ? buffer : Arrays.copyOf(buffer, bytesRead);

	                        xorPayload(actualChunk, xorKey);

	                        String base64String = Base64.getEncoder().encodeToString(actualChunk);
	                        currentPackageChunks.add(base64String);

	                        if (currentPackageChunks.size() == chunksPerFile) {
	                            int startingChunkIdx = chunkCounter - (chunksPerFile - 1);
	                            writeMarkdownPackage(packageCounter++, startingChunkIdx, currentPackageChunks);
	                            currentPackageChunks.clear();
	                        }
	                        chunkCounter++;
	                    }

	                    if (!currentPackageChunks.isEmpty()) {
	                        int startingChunkIdx = chunkCounter - currentPackageChunks.size();
	                        writeMarkdownPackage(packageCounter, startingChunkIdx, currentPackageChunks);
	                    }

	                    System.out.println("Successfully packed into " + (currentPackageChunks.isEmpty() ? packageCounter - 1 : packageCounter) + " package(s).");
	                }
	            } catch (IOException e) {
	                System.err.println("Execution failed: " + e.getMessage());
	            }
	        }

	        private void writeMarkdownPackage(int packageNum, int startChunkNum, List<String> chunks) throws IOException {
	            String fileName = String.format("package%03d.md", packageNum);
	            Path outputPath = outputDir.toPath().resolve(fileName);

	            try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
	                writer.write("# Archive Package " + String.format("%03d", packageNum) + "\n");
	                writer.write("This is an automatically generated system package.\n\n");

	                for (int i = 0; i < chunks.size(); i++) {
	                    int globalChunkId = startChunkNum + i;
	                    writer.write(String.format("Chunk %04d:\n", globalChunkId));
	                    writer.write("```text\n");
	                    writer.write(chunks.get(i));
	                    writer.write("\n```\n\n");
	                }
	            }
	        }
	    }

	    // --- UNPACK COMMAND ---
	    @Command(name = "unpack", description = "Extracts chunks from sorted Markdown files to reconstruct the binary file.")
	    static class UnpackCommand implements Runnable {
	        @Parameters(index = "0", description = "The directory containing the .md source packages.")
	        private File sourceDir;

	        @Option(names = {"-o", "--output-file"}, description = "Target output file path.", required = true)
	        private File outputFile;

	        @Option(names = {"-k", "--key"}, description = "XOR key byte for de-obfuscation.", defaultValue = "42")
	        private byte xorKey;

	        @Override
	        public void run() {
	            if (!sourceDir.exists() || !sourceDir.isDirectory()) {
	                System.err.println("Error: Source must be a valid directory containing MD files.");
	                return;
	            }

	            try {
	                File[] mdFiles = sourceDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".md"));
	                if (mdFiles == null || mdFiles.length == 0) {
	                    System.err.println("Error: No markdown files found in the source directory.");
	                    return;
	                }
	                Arrays.sort(mdFiles, Comparator.comparing(File::getName));

	                // Регулярное выражение корректно извлекает блоки данных независимо от их размера
	                Pattern chunkPattern = Pattern.compile("Chunk \\d{4}:\\s*\\n```text\\s*\\n([^`]+)\\n```");

	                try (OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {
	                    for (File file : mdFiles) {
	                        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
	                        Matcher matcher = chunkPattern.matcher(content);

	                        while (matcher.find()) {
	                            String base64Data = matcher.group(1).strip();
	                            byte[] obfuscatedBytes = Base64.getDecoder().decode(base64Data);
	                            xorPayload(obfuscatedBytes, xorKey);
	                            os.write(obfuscatedBytes);
	                        }
	                    }
	                    System.out.println("Reconstruction complete: " + outputFile.getAbsolutePath());
	                }
	            } catch (IOException | IllegalArgumentException e) {
	                System.err.println("Unpacking failed: " + e.getMessage());
	            }
	        }
	    }

	    private static void xorPayload(byte[] data, byte key) {
	        for (int i = 0; i < data.length; i++) {
	            data[i] ^= key;
	        }
	    }

}