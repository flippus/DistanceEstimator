class MyLog
  def self.debug(message=nil)
    @@my_log ||= Logger.new("#{Rails.root}/log/my.log")
    @@my_log.debug("#{Time.now}" + " " + message) unless message.nil?
  end
end