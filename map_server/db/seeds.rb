# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

training1 = Training.create!
Location.create(training_id: training1.id, longitude: 50, latitude: 50)
Location.create(training_id: training1.id, longitude: 60, latitude: 60)
training2 = Training.create!
Location.create(training_id: training2.id, longitude: 10, latitude: 50)
Location.create(training_id: training2.id, longitude: 10, latitude: 60)
