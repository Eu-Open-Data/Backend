package euopendata.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import euopendata.backend.repositories.AlbumRepository;

@Service
public class AlbumService {
	private final AlbumRepository albumRepository;
	
	@Autowired
	public AlbumService (AlbumRepository albumRepository) {
		this.albumRepository = albumRepository;
	}
}
