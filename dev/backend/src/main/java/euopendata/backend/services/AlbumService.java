package euopendata.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import euopendata.backend.models.Photo;
import euopendata.backend.repositories.AlbumRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AlbumService {
	private final AlbumRepository albumRepository;
	private final JwtService jwtService;
	private final UsersService usersService;

	@Autowired
	public AlbumService (AlbumRepository albumRepository, JwtService jwtService, UsersService usersService) {
		this.albumRepository = albumRepository;
        this.jwtService = jwtService;
        this.usersService = usersService;
    }

	public ResponseEntity<String> addPhoto(Photo photo, String token) {
		try {
			String username = jwtService.extractUsername(token.replace("Bearer ", ""));
			Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
			photo.setUserId(userId);
			albumRepository.save(photo);
			return new ResponseEntity<>("Photo added successfully.", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Photo could not be added.", HttpStatus.BAD_REQUEST);
		}
	}
	public ResponseEntity<?> getAllPhotosByToken(String token) {
		String username = jwtService.extractUsername(token.replace("Bearer ", ""));
		Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
		List<Photo> photos = albumRepository.findAllByUserId(userId);

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

		albumRepository.deleteByUserIdAndId(userId,id);
		return new ResponseEntity<>("Photo deleted successfully.", HttpStatus.OK);

	}

	public ResponseEntity<String> updatePhoto(String token, Integer id, Photo newPhoto ) {
		String username = jwtService.extractUsername(token.replace("Bearer ", ""));
		Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());

		return albumRepository.findById(id)
				.map(photo -> {
					if (!photo.getUserId().equals(userId)) {
						return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
					}
					photo.setUrl(newPhoto.getUrl());
					albumRepository.save(photo);
					return new ResponseEntity<>("Photo updated.", HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>("Photo doesn't exists.", HttpStatus.NOT_FOUND));
	}
}
