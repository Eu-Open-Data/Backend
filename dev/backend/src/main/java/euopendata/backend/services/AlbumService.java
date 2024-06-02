package euopendata.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public void addPhoto (Photo photo, String token) {
		Integer userId = Math.toIntExact(usersService.getUserByUsername(jwtService.extractUsername(token)).getId());
		photo.setUserId(userId);
		
		albumRepository.save(photo);
	}
	public List<Photo> getAllPhotosByToken(String token) {
		String username = jwtService.extractUsername(token.replace("Bearer ", ""));
		Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
		return albumRepository.findAllByUserId(userId);
	}

	public void deletePhoto(String token, int id) {
		String username = jwtService.extractUsername(token.replace("Bearer ", ""));
		Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
		albumRepository.deleteByUserIdAndId(userId,id);
	}
}
