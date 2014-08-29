class AddTrainingIdToLocation < ActiveRecord::Migration
  def change
    add_column :locations, :latitude, :decimal
    add_column :locations, :longitude, :decimal
    add_column :locations, :date, :datetime
    add_column :locations, :training_id, :integer
    add_index :locations, :training_id
  end
end
