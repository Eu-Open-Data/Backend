package euopendata.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import euopendata.backend.models.Photo;
import euopendata.backend.services.AlbumService;

import java.util.List;


@RestController
@RequestMapping(path = "/album")
public class AlbumController {
	private final AlbumService albumService;
	
	@Autowired
	public AlbumController (AlbumService albumService) {
		this.albumService = albumService;
	}
	
	@PostMapping("/photo")
	public ResponseEntity<String> addPhoto(@RequestBody Photo photo,@RequestHeader("Authorization") String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
		return albumService.addPhoto(photo,token);
	}

	@GetMapping("/all-photos")
	public ResponseEntity<?> getAllPhotos(@RequestHeader("Authorization") String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
		return albumService.getAllPhotosByToken(token);
	}

	@DeleteMapping("/photo/{id}")
	public ResponseEntity<String> deletePhoto(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int id) {
		String token = authorizationHeader.replace("Bearer ", "");
		return albumService.deletePhoto(token, id);
	}

	@PutMapping("/photo/{id}")
	public ResponseEntity<String> updatePhoto(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer id, @RequestBody Photo photo) {
		String token = authorizationHeader.replace("Bearer ", "");
		return albumService.updatePhoto(token, id, photo);
	}
}
