A demo function looks like:

```java
	@Test
	void testUnpixel() throws Exception {
		final var count = 51;
		final var file = Path.of("S:/Install/Software/Dev/eclipse.exe.7z");
		final var source = Path.of("S:/Install/Software/Dev/data");
		final var pattern = "page%d.png";

		try (final var out = newByteChannel(file, CREATE, TRUNCATE_EXISTING, WRITE)) {
			for (int i = 0; i < count; i++) {
				final var path = source.resolve(format(pattern, i));

				log().info("Reading chunk %d from the file `%s`...", i, path);

				final var in = ImageIO.read(path.toFile());

				for (int y = 0; y < 4096; y++) {
					var array = new byte[4096];

					for (int x = 0; x < 4096; x++) {
						array[x] = (byte)in.getRGB(x, y);
					}



					if (array[0] == 0 && array[1] == 0 && array[2] == 0) {
						System.out.println("an end identified");
						break;
					}
					out.write(ByteBuffer.wrap(array));
				}
			}
		}
```

Plus, such code needs next imports:

```java
import static java.lang.String.format;
import static java.lang.System.arraycopy;
import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.newByteChannel;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Random;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
```
