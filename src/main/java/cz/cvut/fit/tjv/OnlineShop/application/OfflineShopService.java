package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import cz.cvut.fit.tjv.OnlineShop.persistent.OfflineShopRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OfflineShopService implements OfflineShopServiceInterface {

    private OfflineShopRepository offlineShopRepository;

    public OfflineShopService(OfflineShopRepository offlineShopRepository) {
        this.offlineShopRepository = offlineShopRepository;
    }

    @Override
    public OfflineShop getOfflineShopById(Long id) throws EntityNotFoundException {
        return offlineShopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("OfflineShop with given id: "+ id +" not found!"));
    }

    @Override
    public List<OfflineShop> getOfflineShops() {
        return offlineShopRepository.findAll();
    }

    @Override
    public OfflineShop createOfflineShop(OfflineShop offlineShop) throws IllegalArgumentException {
        if (offlineShop.getId() != null){
            throw new IllegalArgumentException("Entity must have id equals null while creation!");
        }
        if (!offlineShopRepository.existsOfflineShopByNameAndAddress(offlineShop.getName(), offlineShop.getAddress())){
            return offlineShopRepository.save(offlineShop);
        }
        throw new IllegalArgumentException("Offline Shop with name: "+ offlineShop.getName() + " and address: "+ offlineShop.getAddress() +" already exists");
    }

    @Override
    public OfflineShop updateOfflineShop(OfflineShop offlineShop) throws EntityNotFoundException, IllegalArgumentException {
        if (offlineShopRepository.existsById(offlineShop.getId())) {
            if (!offlineShopRepository.existsOfflineShopByNameAndAddress(offlineShop.getName(), offlineShop.getAddress())) {
                return offlineShopRepository.save(offlineShop);
            }
            throw new IllegalArgumentException("Updated offline shop with name: " + offlineShop.getName() + " and description: " + offlineShop.getAddress() + " already exists");
        }
        throw new EntityNotFoundException("Offline shop with id: " + offlineShop.getId() + " does not exists!");
    }

    @Override
    public void deleteOfflineShopById(Long offlineShop_id) throws EntityNotFoundException {
        if (offlineShopRepository.existsById(offlineShop_id)) {
            offlineShopRepository.deleteById(offlineShop_id);
        } else {
            throw new EntityNotFoundException("Offline Shop with id: " + offlineShop_id + " does not exists!");
        }
    }
}
