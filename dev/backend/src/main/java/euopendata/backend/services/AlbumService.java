package euopendata.backend.services;

import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import euopendata.backend.models.Hotel;
import euopendata.backend.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import euopendata.backend.models.Photo;
import euopendata.backend.repositories.AlbumRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final HotelRepository hotelRepository;
	private final JwtService jwtService;
	private final UsersService usersService;
	private final String BUCKET_NAME = "holidayadvisoralbums.appspot.com";
	private final Storage storage;

	@Autowired
	public AlbumService (HotelRepository hotelRepository,AlbumRepository albumRepository, JwtService jwtService, UsersService usersService) throws IOException {
		this.albumRepository = albumRepository;
		this.jwtService = jwtService;
		this.usersService = usersService;
		this.hotelRepository = hotelRepository;

		InputStream inputStream = AlbumService.class.getClassLoader().getResourceAsStream("serviceAccount.json");
		Credentials credentials = GoogleCredentials.fromStream(inputStream);
		this.storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
	}

	public String uploadFile(MultipartFile multipartFile, Integer userId, Long hotelId) throws IOException {
		String fileName = multipartFile.getOriginalFilename();
		File file = convertToFile(multipartFile, fileName);
		String filePath = userId + "/" + hotelId + "/" + fileName;
		BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		storage.create(blobInfo, Files.readAllBytes(file.toPath()));
		file.delete();
		String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";
		return String.format(DOWNLOAD_URL, BUCKET_NAME, URLEncoder.encode(filePath, StandardCharsets.UTF_8));
	}


	private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
		File tempFile = new File(fileName);
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(multipartFile.getBytes());
		}
		return tempFile;
	}

	public ResponseEntity<String> addPhoto(String token, MultipartFile file, Long hotelId) {
		try {
			String username = jwtService.extractUsername(token.replace("Bearer ", ""));
			Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());

			if(!hotelRepository.existsById(hotelId)) {
				return new ResponseEntity<>("Hotel id doesn't exist.", HttpStatus.NOT_FOUND);
			}
			String fileUrl = uploadFile(file, userId, hotelId);
			Hotel hotel = hotelRepository.findById(hotelId).get();
			albumRepository.save(new Photo(fileUrl, userId, hotel));

			return new ResponseEntity<>(fileUrl, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Photo could not be added.", HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<?> getAllPhotosByToken(String token) {
		String username = jwtService.extractUsername(token.replace("Bearer ", ""));
		Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());

		List<Photo> photos = albumRepository.findByUserId(userId);

		if (photos.isEmpty()) {
			return new ResponseEntity<>("No photos found for the user.", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(photos, HttpStatus.OK);
		}
	}

	public ResponseEntity<String> deletePhoto(String token, int id) {
		String username = jwtService.extractUsername(token.replace("Bearer ", ""));
		Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());

		if(!albumRepository.existsById(id)) {
			return new ResponseEntity<>("Photo id doesn't exist.", HttpStatus.NOT_FOUND);
		}
		Photo photo=(albumRepository.findById(id)).get();

		if (!photo.getUserId().equals(userId)) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

		albumRepository.deleteByUserIdAndId(userId,id);
		return new ResponseEntity<>("Photo deleted successfully.", HttpStatus.OK);

	}

	public ResponseEntity<String> updatePhoto(String token, Integer id, MultipartFile file, Long hotelId ) {
		try {
			String username = jwtService.extractUsername(token.replace("Bearer ", ""));
			Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());

			if (!hotelRepository.existsById(hotelId)) {
				return new ResponseEntity<>("Hotel id doesn't exist.", HttpStatus.NOT_FOUND);
			}


			String fileUrl = uploadFile(file, userId, hotelId);

			return albumRepository.findById(id)
					.map(photo -> {
						if (!photo.getUserId().equals(userId)) {
							return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
						}
						photo.setUrl(fileUrl);
						albumRepository.save(photo);
						return new ResponseEntity<>("Photo updated. " + fileUrl, HttpStatus.OK);
					})
					.orElseGet(() -> new ResponseEntity<>("Photo doesn't exists.", HttpStatus.NOT_FOUND));
		}
		catch (Exception e) {
			return new ResponseEntity<>("Photo could not be updated.", HttpStatus.BAD_REQUEST);
		}

	}
}
