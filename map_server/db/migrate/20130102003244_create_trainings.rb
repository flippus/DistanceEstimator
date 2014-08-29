class CreateTrainings < ActiveRecord::Migration
  def change
    create_table :trainings do |t|

      t.timestamps
    end
  end
end
