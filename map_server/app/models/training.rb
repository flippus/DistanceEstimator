class Training < ActiveRecord::Base
  has_many :locations

  default_scope { order('created_at ASC') }

  def get_longitude_center
    return 0 if locations.count < 1
    longitudes = locations.sum(:longitude)
    return longitudes / locations.count
  end

  def get_latitude_center
    return 0 if locations.count < 1
    latitudes = locations.sum(:latitude)
    return latitudes / locations.count
  end
end
