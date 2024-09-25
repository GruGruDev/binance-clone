package com.triquang.binance.service;

import java.util.List;

import com.triquang.binance.model.Asset;
import com.triquang.binance.model.Coin;
import com.triquang.binance.model.User;

public interface AssetService {
	Asset createAsset(User user, Coin coin, double quantity);

	Asset getAssetById(Long assetId);

	Asset getAssetByUserAndId(Long userId, Long assetId);

	List<Asset> getUsersAssets(Long userId);

	Asset updateAsset(Long assetId, double quantity) throws Exception;

	Asset findAssetByUserIdAndCoinId(Long userId, String coinId) throws Exception;

	void deleteAsset(Long assetId);
}
